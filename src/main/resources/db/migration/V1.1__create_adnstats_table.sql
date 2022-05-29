create table public.tbl_dna_validations_stats(
    id serial primary key,
    mutant_qty numeric,
    human_qty numeric,
    ratio numeric
);