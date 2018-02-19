AuthSch Java API
===

## Usage

### Getting started

- You need to have an auth.sch account.
- Register a new application.
- Include the api to your project
- Note tthat this project is depends on gson library:

```XML
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.2</version>
    </dependency>
```

If you're using a web container, you might need to put a [gson-2.8.2.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/) into the `WEB_INF/lib` folder.

### Initialize

```java
    AuthSchAPI api = new AuthSchAPI();
    api.setClientIdentifier("20 digit number");
    api.setClientKey("80 chars key");
```

Store the api instance in application scope.

### Generate url example

```java
    api.generateLoginUrl(hashedSessionId, Scope.BASIC, Scope.GIVEN_NAME, Scope.MAIL));
```

You can also use a list of Scopes.

### Validate code and get tokens

```java
    AuthResponse auth = api.validateAuthentication(code);
```

### Refresh the token

It will return a new AuthRespone instance.

```java
    AuthResponse auth = api.refreshToken(refreshToken);
```


### Get profile data

```java
    ProfileDataResponse profile = api.getProfile(accessToken);
```

## Maven shade

TODO

## List of projects

- beugro.sch (Not released)

> Submit your of if you have.

## License

 "THE BEER-WARE LICENSE" (Revision 42): <br>
<br>
 &lt;gerviba@gerviba.hu&gt; wrote this file. As long as you retain this notice you <br>
can do whatever you want with this stuff. If we meet some day, and you think <br>
this stuff is worth it, you can buy me a beer in return.       Szabó Gergely <br>
