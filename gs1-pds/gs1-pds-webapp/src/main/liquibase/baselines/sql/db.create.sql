DROP TABLE IF EXISTS app_subscription;
DROP TABLE IF EXISTS audit_event;
DROP TABLE IF EXISTS gb_account;
DROP TABLE IF EXISTS gcp_record;
DROP TABLE IF EXISTS import_file;
DROP TABLE IF EXISTS import_record;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS invoice_extra;
DROP TABLE IF EXISTS iso_country_ref;
DROP TABLE IF EXISTS next_id;
DROP TABLE IF EXISTS order_line_item;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS sales_order;

DROP SEQUENCE IF EXISTS app_subscription_id_seq;
DROP SEQUENCE IF EXISTS audit_event_id_seq;
DROP SEQUENCE IF EXISTS country_code_seq  ;
DROP SEQUENCE IF EXISTS gb_account_id_seq;
DROP SEQUENCE IF EXISTS gcp_record_id_seq;
DROP SEQUENCE IF EXISTS import_file_id_seq;
DROP SEQUENCE IF EXISTS import_record_id_seq;
DROP SEQUENCE IF EXISTS invoice_id_seq;
DROP SEQUENCE IF EXISTS invoice_extra_id_seq;
DROP SEQUENCE IF EXISTS next_id_id_seq;
DROP SEQUENCE IF EXISTS order_line_item_id_seq;
DROP SEQUENCE IF EXISTS payment_id_seq;
DROP SEQUENCE IF EXISTS product_id_seq;
DROP SEQUENCE IF EXISTS sales_order_id_seq;


CREATE TABLE app_subscription (
    id integer NOT NULL,
    gb_account_gln text,
    app_id text,
    subscribed_by_agent_username text,
    subscribed_by_username text,
    subscription_date timestamp with time zone,
    attributes text
);

CREATE SEQUENCE app_subscription_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE app_subscription_id_seq OWNED BY app_subscription.id;

CREATE TABLE audit_event (
    id integer NOT NULL,
    type text,
    date timestamp with time zone,
    agent_username text,
    username text,
    gb_account_gln text,
    gtin text,
    invoice_id text,
    details text
);

CREATE SEQUENCE audit_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE audit_event_id_seq OWNED BY audit_event.id;


CREATE SEQUENCE country_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999
    CACHE 1;

CREATE TABLE gb_account (
    id integer NOT NULL,
    gb_account_gln text,
    name text,
    gcps text,
    attributes text
);

CREATE SEQUENCE gb_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE gb_account_id_seq OWNED BY gb_account.id;

CREATE TABLE gcp_record (
    id integer NOT NULL,
    gcp text,
    gln text
);

CREATE SEQUENCE gcp_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE gcp_record_id_seq OWNED BY gcp_record.id;

CREATE TABLE import_file (
    id integer NOT NULL,
    content bytea
);

CREATE SEQUENCE import_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE import_file_id_seq OWNED BY import_file.id;

CREATE TABLE import_record (
    id integer NOT NULL,
    gb_account_gln text,
    import_file_id text,
    filename text,
    format text,
    upload_date timestamp with time zone,
    validated_date timestamp with time zone,
    confirmed_date timestamp with time zone,
    status smallint,
    prevalidation text,
    validation text
);

CREATE SEQUENCE import_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE import_record_id_seq OWNED BY import_record.id;

CREATE TABLE invoice (
    id integer NOT NULL,
    order_status smallint,
    gb_account_gln text,
    invoice_id text,
    date timestamp with time zone,
    due_date timestamp with time zone,
    description text,
    total_value numeric,
    total_currency text,
    balance_due_value numeric,
    balance_due_currency text,
    billing_reference text,
    payment_id text,
    billed_date timestamp with time zone,
    payment_committed_date timestamp with time zone,
    paid_date timestamp with time zone
);

CREATE TABLE invoice_extra (
    id integer NOT NULL,
    invoice_id integer,
    invoice_extra_index integer,
    item_code text,
    item_description text,
    item_parameters text,
    total_value numeric,
    total_currency text
);

CREATE SEQUENCE invoice_extra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE invoice_extra_id_seq OWNED BY invoice_extra.id;

CREATE SEQUENCE invoice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE invoice_id_seq OWNED BY invoice.id;

CREATE TABLE iso_country_ref (
    id integer DEFAULT nextval('country_code_seq'::regclass) NOT NULL,
    country_name character varying(100),
    country_code_txt2 character varying(2),
    country_code_txt3 character varying(3),
    country_code_num character varying(3),
    enabled character varying(1),
    modified_date timestamp with time zone
);

CREATE TABLE next_id (
    id integer NOT NULL,
    name text,
    next_id integer
);

CREATE SEQUENCE next_id_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE next_id_id_seq OWNED BY next_id.id;

CREATE TABLE order_line_item (
    id integer NOT NULL,
    sales_order_id integer,
    order_line_item_index integer,
    quantity integer,
    item_code text,
    item_description text,
    item_parameters text,
    price_value numeric,
    price_currency text,
    total_value numeric,
    total_currency text
);


CREATE SEQUENCE order_line_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE order_line_item_id_seq OWNED BY order_line_item.id;

CREATE TABLE payment (
    id integer NOT NULL,
    gb_account_gln text,
    payment_id text,
    date timestamp with time zone,
    payment_receipt_id text,
    payment_receipt_date timestamp with time zone,
    amount_value numeric,
    amount_currency text,
    description text,
    status smallint,
    paid_reference text
);

CREATE SEQUENCE payment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE payment_id_seq OWNED BY payment.id;

CREATE TABLE product (
    id integer NOT NULL,
    gb_account_gln text,
    gtin text,
    data_accuracy_ack_user text,
    product_attributes text,
    registered_date timestamp with time zone,
    modified_date timestamp with time zone,
    next_action_date timestamp with time zone,
    pending_next_action_date timestamp with time zone,
    pending_order_id_set text,
    target_country_code_id integer,
    gpc_category_code text
);

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE product_id_seq OWNED BY product.id;

CREATE TABLE sales_order (
    id integer NOT NULL,
    date timestamp with time zone,
    order_id text,
    gb_account_gln text,
    po_id text,
    summary text,
    total_value numeric,
    total_currency text,
    balance_due_value numeric,
    balance_due_currency text,
    invoice_id text
);

CREATE SEQUENCE sales_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE sales_order_id_seq OWNED BY sales_order.id;

ALTER TABLE ONLY app_subscription ALTER COLUMN id SET DEFAULT nextval('app_subscription_id_seq'::regclass);
ALTER TABLE ONLY audit_event ALTER COLUMN id SET DEFAULT nextval('audit_event_id_seq'::regclass);
ALTER TABLE ONLY gb_account ALTER COLUMN id SET DEFAULT nextval('gb_account_id_seq'::regclass);
ALTER TABLE ONLY gcp_record ALTER COLUMN id SET DEFAULT nextval('gcp_record_id_seq'::regclass);
ALTER TABLE ONLY import_file ALTER COLUMN id SET DEFAULT nextval('import_file_id_seq'::regclass);
ALTER TABLE ONLY import_record ALTER COLUMN id SET DEFAULT nextval('import_record_id_seq'::regclass);
ALTER TABLE ONLY invoice ALTER COLUMN id SET DEFAULT nextval('invoice_id_seq'::regclass);
ALTER TABLE ONLY invoice_extra ALTER COLUMN id SET DEFAULT nextval('invoice_extra_id_seq'::regclass);
ALTER TABLE ONLY next_id ALTER COLUMN id SET DEFAULT nextval('next_id_id_seq'::regclass);
ALTER TABLE ONLY order_line_item ALTER COLUMN id SET DEFAULT nextval('order_line_item_id_seq'::regclass);
ALTER TABLE ONLY payment ALTER COLUMN id SET DEFAULT nextval('payment_id_seq'::regclass);
ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);
ALTER TABLE ONLY sales_order ALTER COLUMN id SET DEFAULT nextval('sales_order_id_seq'::regclass);
