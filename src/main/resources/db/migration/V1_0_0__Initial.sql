create table habit (id int8 generated by default as identity, created_at timestamp, edited_at timestamp, title varchar(255), primary key (id));
create table journal_entry (id int8 generated by default as identity, created_at timestamp, description varchar(255), edited_at timestamp, habit_id int8, primary key (id));
alter table if exists journal_entry add constraint FK3045ke4o4mm4fmj9151ltyu0 foreign key (habit_id) references habit;
