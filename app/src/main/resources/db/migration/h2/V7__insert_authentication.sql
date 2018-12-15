INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
	VALUES ('oauth2-demo', 'resource-server-rest-api',
	/*oauth2-demo-password*/'$2a$08$p3YaoR2zerXJwqfff5JuwOm46GNkH85K/1U82.or4zfbPRPfzr1qa',
	'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);