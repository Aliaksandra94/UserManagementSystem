INSERT INTO roles VALUES
(1, 'ADMIN'),
(2, 'USER');
insert into users_account values (1, '2021-06-14', 'Admin', 'Adminovich', '$2a$08$QwVTDqpcEMG/dawRHZwoMePZ8JEPfcbXYdXOVtUmN9rDZfhDp3BWu', 0, 'ADMIN');
insert into users_account_users_role_list values (1, 1);
insert into users_account values (2,'2021-06-14', 'User', 'Userovich', '$2a$08$M8JK1Q9T5Rhz./tcIkt3.eqvKeY9DAOZLN0oUEUcNDP0UiXzzWv/i', 0, 'USER');
insert into users_account_users_role_list values (2, 2);
insert into users_account values (3, '2021-06-14','Userio', 'Useriovich', '$2a$08$29tkL2M/aAfX19qHw.4AQOdTt957sg6YMC8fpEAJOaZNpPJyuWCuW', 1, 'USERIO');
insert into users_account_users_role_list values (3, 2);