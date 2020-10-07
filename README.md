# fake-news
Proxy server that changes content of HTTP traffic.

# Tests
- [Scenario 1 - text file](http://zebroid.ida.liu.se/fakenews/test1.txt)
- [Scenario 2 - HTML file](http://zebroid.ida.liu.se/fakenews/test2.html)
- [Scenario 3 - HTML with link](http://zebroid.ida.liu.se/fakenews/test3.html)
- [Scenario 4 - HTML with embedded images](http://zebroid.ida.liu.se/fakenews/test4.html)

## Using telnet
- after running the proxy, telnet into it:
```
telnet localhost 8080
```
- After successful connection, paste the following request to get simple text through HTTP
**INCLUDING THE EMPTY LINE**:
```
GET /fakenews/test1.txt HTTP/1.1
host: zebroid.ida.liu.se

```
- The response should look like this:
```
HTTP/1.1 200 OK
Date: Tue, 22 Sep 2020 16:08:35 GMT
Server: Apache/2.4.6 (CentOS) mod_auth_gssapi/1.5.1 mod_nss/1.0.14 NSS/3.28.4 mod_wsgi/3.4 Python/2.7.5
Last-Modified: Sun, 30 Aug 2020 16:17:02 GMT
ETag: "17f-5ae1a9db7bb80"
Accept-Ranges: bytes
Content-Length: 383
Content-Type: text/plain; charset=UTF-8

This is a basic text file.
It tells a simple story about our yellow friend Smiley,
who is from Stockholm. Smiley is round, I think.

Without your proxy, you should be able to view this page just fine.

With your proxy, this page should look a bit different,
with all mentions of Smiley from Stockholm
being changed into something else.  
The page should still be formatted properly.
```