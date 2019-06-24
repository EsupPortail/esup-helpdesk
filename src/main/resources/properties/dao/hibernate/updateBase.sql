UPDATE `h_acti` SET scop='INVITED' where scop='DEFAULT';
UPDATE `h_depa` SET visi='' where visi IS NULL;
UPDATE `h_cate` SET cate_confi = false;
UPDATE `h_cate` SET cate_invi = false;
UPDATE `h_depa` SET confi = false;
UPDATE `h_depa` SET anon = false;
UPDATE `h_tick` SET anon = false;
