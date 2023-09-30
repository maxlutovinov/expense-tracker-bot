--liquibase formatted sql
--changeset <maxlutovinov>:<create-expense-category-seq>
CREATE SEQUENCE IF NOT EXISTS public.expense_category_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.expense_category_id_seq;
