create table public.tbl_dna_validations(
    dna text primary key,
    is_mutant bool NOT NULL,
    created_at timestamp default now()
);