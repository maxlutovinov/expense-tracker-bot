--liquibase formatted sql
--changeset <maxlutovinov>:<create-user-account-table>
CREATE TABLE IF NOT EXISTS public.user_account(
    telegram_user_id bigint NOT NULL,
    user_name character varying(256),
    first_name character varying(256),
    last_name character varying(256),
    PRIMARY KEY (telegram_user_id)
);

--rollback DROP TABLE public.user_account;
