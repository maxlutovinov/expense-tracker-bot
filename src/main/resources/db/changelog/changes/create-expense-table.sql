--liquibase formatted sql
--changeset <maxlutovinov>:<create-expense-table>
CREATE TABLE IF NOT EXISTS public.expense(
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    expense_date date NOT NULL DEFAULT CURRENT_DATE,
    cost numeric NOT NULL,
    comment character varying(256),
    user_id bigint NOT NULL,
    PRIMARY KEY (id)
);

--rollback DROP TABLE public.expense;
