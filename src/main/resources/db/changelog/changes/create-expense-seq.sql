--liquibase formatted sql
--changeset <maxlutovinov>:<create-expense-seq>
create sequence IF NOT EXISTS public.expense_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.expense_id_seq;
