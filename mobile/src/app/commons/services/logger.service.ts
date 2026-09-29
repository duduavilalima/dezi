import { Injectable } from "@angular/core";
import { isSomething } from "../../utils/guards";
import { Constructor, Omit, NameLike, toName, InferInstance } from "../../utils/typescript";
import { environment } from "../../../environments/environment.prod";

export type LogMessageLike = string | ((entry: LogEntry) => string);

export enum LogLevel {
	TRACE = -2,
	DEBUG = -1,
	INFO = 0,
	WARN,
	ERROR,
	FATAL,
}

export interface LoggableClass<T = any> extends Constructor<T> {
	loggerId?: string;
	exceptionLevel?: LogLevel;
}

export interface LoggerConfig {
	id: string;
	name: string;
	scope: string[];
	exceptionLevel: LogLevel;
}

export interface LoggerConfigInput {
	id: string;
	name?: string;
	exceptionLevel?: LogLevel;
}

export interface LogEntryInput {
	level: LogLevel;
	message?: LogMessageLike;
	exception?: any;
	values?: any[];
}

export interface ExceptionLogEntryInput extends LogEntryInput {
	exception: any;
}

export interface MessageLogEntryInput extends LogEntryInput {
	message: LogMessageLike;
}

export type LogEntryInputLike = ExceptionLogEntryInput | MessageLogEntryInput;

export interface LogEntry {
	level: LogLevel;
	entryDate: Date;
	values: any[];
	message?: LogMessageLike;
	exception?: any;
}

export interface LogAppenderEntry extends Omit<LogEntry, "message"> {
	message?: string;
}

export interface LogAppender {
	appendLogEntry(config: LoggerConfig, entry: LogAppenderEntry): void;
}

export class Logger {

	constructor(
		public readonly config: Readonly<LoggerConfig>,
		private readonly appenders: ReadonlyArray<LogAppender>,
	) {}

	atMethod<C extends Constructor<any>>(
		instance: C,
		method: Function | keyof C | keyof C["prototype"],
	) {
		const name = method instanceof Function ? method.name : String(method);
		return this.at(name);
	}

	atStaticMethod<C extends Constructor<any>>(
		instance: C,
		method: Function | keyof C["prototype"],
	) {
		const name = method instanceof Function ? method.name : String(method);
		return this.at(name);
	}

	atInstanceMethod<C extends Constructor<any>, I extends InferInstance<C> = InferInstance<C>>(
		instance: I,
		method: Function | keyof I,
	) {
		const name = method instanceof Function ? method.name : String(method);
		return this.at(name);
	}

	at(scopeName: NameLike) {
		const scope = [
			...this.config.scope,
			toName(scopeName),
		];
		return new Logger({ ...this.config, scope }, this.appenders);
	}

	entry(entry: LogEntryInputLike) {
		this.doEntry(entry);
	}

	exception(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, this.config.exceptionLevel, message, ...values);
	}

	logException(exception: any, level?: LogLevel, message?: LogMessageLike, ...values: any[]): void {
		level = level || this.config.exceptionLevel;
		let entry: LogEntryInput;
		if (isSomething(message)) {
			entry = {
				level,
				message,
				exception,
				values,
			};
		} else {
			entry = {
				level,
				exception,
				values,
			};
		}
		this.doEntry(entry);
	}

	log(level: LogLevel, message: LogMessageLike, ...values: any[]): void {
		this.entry({
			level,
			message,
			values,
		});
	}

	traceException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.TRACE, message, ...values);
	}

	trace(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.TRACE, message, ...values);
	}

	debugException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.DEBUG, message, ...values);
	}

	debug(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.DEBUG, message, ...values);
	}

	infoException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.INFO, message, ...values);
	}

	info(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.INFO, message, ...values);
	}

	warnException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.WARN, message, ...values);
	}

	warn(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.WARN, message, ...values);
	}

	errorException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.ERROR, message, ...values);
	}

	error(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.ERROR, message, ...values);
	}

	fatalException(exception: any, message?: LogMessageLike, ...values: any[]): void {
		this.logException(exception, LogLevel.FATAL, message, ...values);
	}

	fatal(message: LogMessageLike, ...values: any[]): void {
		this.log(LogLevel.FATAL, message, ...values);
	}

	private doEntry(entry: LogEntryInput) {
		this.doAppend(this.config, {
			...entry,
			values: entry.values || [],
			entryDate: new Date(),
		});
	}

	private doAppend(config: LoggerConfig, entry: LogEntry) {
		let appenderEntry: LogAppenderEntry;
		if ("message" in entry && entry.message !== undefined) {
			let message: string;
			if (entry.message instanceof Function) {
				message = entry.message(entry);
			} else {
				message = entry.message;
			}
			appenderEntry = { ...entry, message };
		} else {
			appenderEntry = entry as any;
		}
		this.appenders.forEach(appender => appender.appendLogEntry(config, appenderEntry));
	}
}


@Injectable()
export class LoggerService {

	minimumLogLevel = LogLevel.DEBUG;
	defaultConfig = {
		exceptionLevel: LogLevel.ERROR,
	};

	private loggers: { [id: string]: Logger } = {};
	private appenders: LogAppender[];

	constructor() {
		this.minimumLogLevel = environment.logLevel;
		this.appenders = [
			// TODO Parametrizar os appenders (talvez injetá-los... mas como?)
			new ConsoleLogAppender(this.shouldSkipLogging),
		];
	}

	create(id: string): Logger;
	create(userClass: LoggableClass): Logger;
	create(input: LoggerConfigInput): Logger;
	create(inputLike: string | LoggableClass | LoggerConfigInput): Logger;
	create(inputLike: string | LoggableClass | LoggerConfigInput) {
		let input: LoggerConfig;
		if (typeof inputLike === "string") {
			input = {
				...this.defaultConfig,
				id: inputLike,
				name: inputLike,
				scope: [],
			};
		} else if (inputLike instanceof Function) {
			input = {
				...this.defaultConfig,
				exceptionLevel: inputLike.exceptionLevel || this.defaultConfig.exceptionLevel,
				id: inputLike.loggerId || inputLike.name,
				name: inputLike.name,
				scope: [],
			};
		} else if (!!inputLike) {
			input = {
				...this.defaultConfig,
				exceptionLevel: inputLike.exceptionLevel || this.defaultConfig.exceptionLevel,
				id: inputLike.id,
				name: inputLike.name || inputLike.id,
				scope: [],
			};
		} else {
			throw new Error("Missing logger configuration.");
		}
		if (!!this.loggers[input.id]) {
			throw new Error(`Logger config for ID ${input.id} is already set.`);
		}
		const logger = new Logger(input, this.appenders);
		this.loggers[input.id] = logger;
		return logger;
	}

	get(id: string): Logger;
	get(userClass: LoggableClass): Logger;
	get(input: LoggerConfigInput): Logger;
	get(inputLike: string | LoggableClass | LoggerConfigInput) {
		const id = this.getId(inputLike);
		const logger = this.loggers[id];
		if (!logger) {
			throw new Error(`Logger with ID ${id} does not exist.`);
		}
		return logger;
	}

	for(id: string): Logger;
	for(userClass: LoggableClass): Logger;
	for(input: LoggerConfigInput): Logger;
	for(inputLike: string | LoggableClass | LoggerConfigInput) {
		const id = this.getId(inputLike);
		const logger = this.loggers[id];
		if (!!logger) return logger;
		return this.create(inputLike);
	}

	private getId(inputLike: string | LoggableClass | LoggerConfigInput) {
		let id: string;
		if (typeof inputLike === "string") {
			id = inputLike;
		} else if (inputLike instanceof Function) {
			id = inputLike.loggerId || inputLike.name;
		} else if (!!inputLike) {
			id = inputLike.id;
		} else {
			throw new Error("Missing logger ID.");
		}
		return id;
	}

	private shouldSkipLogging = (level: LogLevel) => level >= this.minimumLogLevel;
}

class ConsoleLogAppender implements LogAppender {

	constructor(
		private shouldSkipLogging: (level: LogLevel) => boolean,
	) {}

	appendLogEntry(config: LoggerConfig, entry: LogAppenderEntry): void {
		const level = entry.level;
		if (this.shouldSkipLogging(level)) return;
		switch(entry.level) {
		case LogLevel.TRACE:
		case LogLevel.DEBUG:
			this.doLog("debug", config, entry);
			break;
		case LogLevel.INFO:
			this.doLog("info", config, entry);
			break;
		case LogLevel.WARN:
			this.doLog("warn", config, entry);
			break;
		case LogLevel.ERROR:
		case LogLevel.FATAL:
			this.doLog("error", config, entry);
			break;
		}
	}

	private doLog(
		level: 'debug' | 'info' | 'warn' | 'error',
		config: LoggerConfig,
		entry: LogAppenderEntry,
	) {
		const rest: any[] = [];
		let prefix = `[${config.id}]`;
		if (config.scope.length > 0) {
			config.scope.forEach(scopeName => {
				prefix = `${prefix}[${scopeName}]`;
			});
		}
		if ("message" in entry) {
			rest.push(entry.message);
		}
		if ("exception" in entry) {
			rest.push(entry.exception);
		}
		console[level](prefix, ...rest, ...(entry.values || []));
	}
}

export function isLogMessageLike(value: any): value is LogMessageLike {
	return typeof value === "string" || value instanceof Function;
}
