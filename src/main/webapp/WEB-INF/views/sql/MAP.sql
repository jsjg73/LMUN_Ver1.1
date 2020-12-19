create table route_master(
id varchar2(30),
num number(10),
name varchar2(30),
x_ varchar2(30) ,
y_ varchar2(30),
address varchar2(50),
phone varchar2(30),
place_url varchar2(100),
constraint routem_pk primary key(id)
);

create table route_slave(
rno number(20),
id varchar2(30),
departure varchar2(300),
bus_route varchar2(500),
complex_route varchar2(500),
bus_time varchar2(10),
complex_time varchar2(10),
constraint routes_pk primary key(rno)
);

create sequence route_slave_seq increment by 1 start with 1 nocache;

select * from route;


select * from tab;


delete from sub_rcm where subname = '잠실역2호선';

insert into sub_rcm values('고속터미널역 9호선', '127.004211793489','37.5059814899483');
insert into sub_rcm values('신사역 3호선', '127.020310824772','37.5164350739218');
insert into sub_rcm values('이태원역 6호선','126.994333861918','37.5345252050511');
insert into sub_rcm values('용산역 1호선', '126.96474263398179','37.52977360379245');
insert into sub_rcm values('서울역 1호선','126.972091251236','37.5559802396321');
insert into sub_rcm values('부천역 1호선','126.783081743529','37.4839888842916');
insert into sub_rcm values('혜화역 4호선','127.001942745502','37.5820439179086');
insert into sub_rcm values('광화문역 5호선','126.976423148119','37.5716477085545');
insert into sub_rcm values('홍대입구역 2호선','126.923778562273','37.5568707448873');
insert into sub_rcm values('문래역 2호선','126.894778820701','37.5179757181801');
-- 10  --
insert into sub_rcm values('영등포역 1호선','126.907550274975','37.5156726288261');
insert into sub_rcm values('사당역 2호선','126.981558584016','37.4765604303289');
insert into sub_rcm values('잠실나루역 2호선','127.103808749487','37.5207124124456');
insert into sub_rcm values('잠실역 2호선','127.100228759082','37.513312862699');
insert into sub_rcm values('강남역 2호선','127.028000275071','37.4980854357918');
insert into sub_rcm values('신논현역 9호선','127.025492036104', '37.504811111562');
insert into sub_rcm values('종로3가역 1호선','126.992153252476', '37.570420844523');
insert into sub_rcm values('을지로3가역 2호선','126.9909855674','37.5662905969967');
insert into sub_rcm values('을지로4가역 2호선','126.997632059113','37.5666405038268');
insert into sub_rcm values('동대문역사문화공원역 2호선','127.009005301929','37.5656635044556');
-- 20 --
insert into sub_rcm values('건대입구역 2호선','127.069202917341','37.5404084182632');