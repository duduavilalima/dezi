package br.com.fasolti.queue.enuns.tipos;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.LoggableUserType;

@SuppressWarnings("unused")
//TODO Converter para a nova forma do Hibernate 5
public class CodedEnumType implements EnhancedUserType, DynamicParameterizedType, LoggableUserType, Serializable {

	private static final long serialVersionUID = 3132985511263013854L;

	private static final String ENUM = "enumClass";

	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumClass;

	private int sqlType;

	private Integer getCode(Enum<?> value) {
		Integer result;

		try {
			Method mth = value.getClass().getDeclaredMethod("getCode");
			result = (Integer) mth.invoke(value);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			result = -1;
		}

		return result;
	}

	private Enum<?> valueOf(Integer code) {
		try {
			Method method = enumClass.getDeclaredMethod("valueOf", Integer.class);
			return (Enum<?>) method.invoke(null, code);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { sqlType };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return enumClass;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value; // Enum is immutable, returns the same reference
	}

	@Override
	public boolean isMutable() {
		return false; // Enum is immutable
	}

	@Override
	// Called when a Skill object is stored in Hibernate second level cache
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	// Called when a Skill object is retrieved from Hibernate second level cache
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached; // Returning the same object as is simply a reference to an Enum constant
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original; // Immutable
	}

	@Override
	public String objectToSQLString(Object value) {
		@SuppressWarnings("rawtypes")
		Integer intValue = getCode((Enum) value);
		return intValue.toString();
	}

	@Override
	public String toXMLString(Object value) {
		return objectToSQLString(value);
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		return valueOf(Integer.valueOf(xmlValue));
	}

	@Override
	public String toLoggableString(Object value, SessionFactoryImplementor factory) {
		return objectToSQLString(value);
	}

	@Override
	public void setParameterValues(Properties parameters) {
		final DynamicParameterizedType.ParameterType reader = (DynamicParameterizedType.ParameterType) parameters
				.get(PARAMETER_TYPE);

		Class<?> clazz;

		if (reader != null) {
			clazz = reader.getReturnedClass();
		} else {
			String className = (String) parameters.get(ENUM);
			try {
				clazz = ReflectHelper.classForName(className, this.getClass());
			} catch (ClassNotFoundException exception) {
				throw new HibernateException("Enum class not found", exception);
			}
		}

		enumClass = clazz.asSubclass(Enum.class);
		sqlType = Types.INTEGER;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		Integer code = rs.getInt(names[0]);

		if (rs.wasNull()) {
			return null;
		}

		return valueOf(code);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (null == value) {
			st.setNull(index, sqlType);
		} else {
			st.setObject(index, getCode((Enum<?>) value), sqlType);
		}

	}
}