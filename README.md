This is a github version of the code hosted here
http://code.google.com/p/langdetect/

I added mavenification and better simpler loading.

I intend to improve the library in the future. Work can be done
to improve performance, and part of the original lib is not included.

If you use maven, this project is hosted at 

	<repository>
		<id>Github Imaginatio</id>
		<url>https://github.com/Imaginatio/Maven-repository/raw/master</url>
	</repository>
		
To import this artifact add this to your dependencies

	<dependency>
		<groupId>com.cybozu.labs</groupId>
		<artifactId>langdetect</artifactId>
		<version>1.2.1</version>
	</dependency>