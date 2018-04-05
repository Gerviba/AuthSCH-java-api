AuthSch Java API
===

![Coverage: 80.3%](https://img.shields.io/badge/coverage-80.3%25-green.svg)
![Version: 1.0.2](https://img.shields.io/badge/version-1.0.2-blue.svg)
![BEER-WARE](https://img.shields.io/badge/license-BEER--WARE-yellow.svg)

## Usage

### Getting started

- You need to have an auth.sch account.
- Register a new application.
- Include the api to your project.
- Make sure your project has the `jackson-core` and the `jackson-databind` dependency.

Spring boot applications will automatically include the jackson dependencies.

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

## Maven 

### Compiled JAR

pom.xml

```XML

	<properties>
		<authsch.version>1.0.2</authsch.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-core</artifactId>
			<version>2.9.5</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.9.5</version>
		</dependency>
		<dependency>
			<groupId>hu.sch</groupId>
			<artifactId>authsch</artifactId>
			<version>${authsch.version}</version>
			<scope>system</scope>
			<systemPath>${project.basedir}/src/main/webapp/WEB-INF/lib/authsch-${authsch.version}.jar</systemPath>
		</dependency>
	</dependencies>
```

You might need to put the `authsch-x.x.x.jar` into the `WEB-INF/lib` folder.


## List of projects

- beugro.sch (Not released)

> Submit your if you have.

## License

 "THE BEER-WARE LICENSE" (Revision 42): <br>
<br>
 &lt;gerviba@gerviba.hu&gt; wrote this file. As long as you retain this notice you <br>
can do whatever you want with this stuff. If we meet some day, and you think <br>
this stuff is worth it, you can buy me a beer in return.       Szabó Gergely <br>
