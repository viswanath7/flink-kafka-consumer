CREATE TABLE IF NOT EXISTS events_by_correlation_id
(
client_ip inet,
correlation_id text,
reference text,
event_type text,
details text,
user_agent text,
user_agent_filtered text,
event_date_time timestamp,
PRIMARY KEY (correlation_id, event_date_time)
) WITH CLUSTERING ORDER BY (event_date_time DESC);

ALTER TABLE events_by_correlation_id WITH COMPACTION = {'class':'DateTieredCompactionStrategy', 
'timestamp_resolution':'MICROSECONDS', 
'base_time_seconds':'3600', 
'max_sstable_age_days':'730'};

CREATE TABLE IF NOT EXISTS events_by_reference_and_year
(
reference text,
year int,
client_ip inet,
correlation_id text,
event_type text,
details text,
user_agent text,
user_agent_filtered text,
event_date_time timestamp,
PRIMARY KEY ((reference, year), event_date_time)
) WITH CLUSTERING ORDER BY (event_date_time DESC);

ALTER TABLE events_by_reference_and_year WITH COMPACTION = {'class':'DateTieredCompactionStrategy', 
'timestamp_resolution':'MICROSECONDS', 
'base_time_seconds':'3600', 
'max_sstable_age_days':'730'};

CREATE TABLE IF NOT EXISTS events_by_type
(
client_ip inet,
correlation_id text,
reference text,
event_type text,
details text,
user_agent text,
user_agent_filtered text,
event_date_time timestamp,
PRIMARY KEY (event_type, event_date_time)
) WITH CLUSTERING ORDER BY (event_date_time DESC);

ALTER TABLE events_by_type WITH COMPACTION = {'class':'DateTieredCompactionStrategy',
'timestamp_resolution':'MICROSECONDS',
'base_time_seconds':'3600',
'max_sstable_age_days':'730'};

INSERT INTO events_by_correlation_id (correlation_id, event_date_time, event_type, client_ip, details, reference, user_agent, user_agent_filtered)
  VALUES ('cfd66ccc-d857-4e90-b1e5-df98a3d40cd6', '2016-02-10 11:00:50', 'WAYF_DISPLAYED', '198.39.100.45', 'login page is displayed', '', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:39.0) Gecko/20100101 Firefox/39.0','FIREFOX39 - MAC_OS_X')
  USING TTL 86400;

INSERT INTO events_by_reference_and_year (reference, year, event_date_time, event_type, correlation_id, client_ip, details, user_agent, user_agent_filtered)
	VALUES ('wenstfhjwwhucm9zme04szfyq0tdykpbtxlmwlbnywm[', 2016, '2016-01-20 10:26:05','LOGIN_INTERCEPTOR_CREATE_SESSION','4ba35527-26c2-4cb8-8ec4-c40b288d62b0','89.255.57.26', 'aegon:7a5d6f13-4b65-4d7b-ba40-cce0a4833d2e', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:43.0) Gecko/20100101 Firefox/43.0','FIREFOX4 - MAC_OS_X')
USING TTL 86400;

INSERT INTO events_by_reference_and_year (reference, year, event_date_time, event_type, correlation_id, client_ip, details, user_agent, user_agent_filtered)
	VALUES ('wenstfhjwwhucm9zme04szfyq0tdykpbtxlmwlbnywm[', 2016, '2016-01-20 10:26:07','USER_ACCESSED_PROFILE','4ba35527-26c2-4cb8-8ec4-c40b288d62b0','89.255.57.26', 'NA', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:43.0) Gecko/20100101 Firefox/43.0','FIREFOX4 - MAC_OS_X')
USING TTL 86400;