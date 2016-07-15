
drop table if exists Entry;
drop table if exists Explaination;

create table Entry(
	word varchar(30) not null,
	numE int,
	constraint pkEntry primary key (word)
);

create table Explaination(
	word varchar(30) not null,
	id int,
	def varchar(500),
	constraint pkExplaination primary key(word, id)
);

insert into Entry values(
	"commencement",
	2
);
insert into Explaination values(
	"commencement",
	1,
	"The commencement of something is its beginning."
);
insert into Explaination values(
	"commencement",
	2,
	"Commencement is a ceremony at a university, college, or high school at which students formally receive their degrees or diplomas."
);