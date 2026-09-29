ALTER TABLE avaliacao
   ADD COLUMN em_liberacao boolean;


ALTER TABLE campanha
   ADD COLUMN av_em_liberacao boolean;

ALTER TABLE avaliacao
   ADD COLUMN av_homeoffice boolean;
