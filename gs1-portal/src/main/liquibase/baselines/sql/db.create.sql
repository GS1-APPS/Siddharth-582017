DROP TABLE IF EXISTS standalonemember;
DROP TABLE IF EXISTS standaloneuser;

DROP SEQUENCE IF EXISTS standalonemember_id_seq;
DROP SEQUENCE IF EXISTS standaloneuser_id_seq;

CREATE TABLE standalonemember (
    id integer NOT NULL,
    gln text,
    company_name text,
    address1 text,
    address2 text,
    city text,
    state text,
    postal_code text,
    member_id text,
    gcps text,
    brand_owner_agreement_signed_by_user text,
    brand_owner_agreement_signed_date timestamp with time zone
);

CREATE SEQUENCE standalonemember_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE standalonemember_id_seq OWNED BY standalonemember.id;

CREATE TABLE standaloneuser (
    id integer NOT NULL,
    username text,
    first_name text,
    last_name text,
    timezone text,
    member integer,
    password text,
    state smallint,
    roles text,
    created timestamp with time zone,
    last_login timestamp with time zone,
    password_reset text,
    password_reset_expiration timestamp with time zone,
    login_count integer,
    api_key text
);

CREATE SEQUENCE standaloneuser_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE standaloneuser_id_seq OWNED BY standaloneuser.id;


ALTER TABLE ONLY standalonemember ALTER COLUMN id SET DEFAULT nextval('standalonemember_id_seq'::regclass);
ALTER TABLE ONLY standaloneuser ALTER COLUMN id SET DEFAULT nextval('standaloneuser_id_seq'::regclass);

ALTER TABLE ONLY standalonemember
    ADD CONSTRAINT standalonemember_pkey PRIMARY KEY (id);
ALTER TABLE ONLY standaloneuser
    ADD CONSTRAINT standaloneuser_pkey PRIMARY KEY (id);

CREATE INDEX standalonemember_index_company ON standalonemember USING btree (company_name);
CREATE INDEX standalonemember_index_gln ON standalonemember USING btree (gln);

CREATE INDEX standaloneuser_index_password_reset ON standaloneuser USING btree (password_reset);
CREATE INDEX standaloneuser_index_username ON standaloneuser USING btree (username);
GRANT ALL ON SCHEMA public TO PUBLIC;
