--liquibase formatted sql
--changeset <maxlutovinov>:<create-expense-category-table>
CREATE TABLE IF NOT EXISTS public.expense_category(
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    PRIMARY KEY (id)
);

--rollback DROP TABLE public.expense_category;
