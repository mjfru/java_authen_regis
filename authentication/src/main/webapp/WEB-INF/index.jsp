<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/css/styles.css">
<title>Login | Registration</title>
</head>
<body>
	<div class="header">
		<h1>Welcome!</h1>
		<h2>Join Our Growing Community</h2>
	</div>
	
	
	<div class="form-container">
		<div class="register">
			<h3>Register</h3>
			<form:form action="/register" method="POST" modelAttribute="newUser">
				<div>
					<form:label path="userName">User Name:</form:label>
					<form:errors path="userName" />
					<form:input type="string" path="userName"/>
				</div>
				<div>
					<form:label path="email">Email:</form:label>
					<form:errors path="email" />
					<form:input type="string" path="email"/>
				</div>
				<div>
					<form:label path="password">Password:</form:label>
					<form:errors path="password" />
					<form:input type="password" path="password"/>
				</div>
				<div>
					<form:label path="confirm">Confirm Password:</form:label>
					<form:errors path="confirm" />
					<form:input type="password" path="confirm"/>
				</div>
				<input type="submit" value="Register User"/>
			</form:form>
		</div>
	
		<div class="login">
			<h3>Login</h3>
			<form:form action="/login" method="POST" modelAttribute="newLogin">
				<div>
					<form:label path="email">Email:</form:label>
					<form:errors path="email"/>
					<form:input type="string" path="email"/>
				</div>
				<div>
					<form:label path="password">Password:</form:label>
					<form:errors path="password" />
					<form:input type="password" path="password"/>
				</div>
				<input type="submit" value="Login"/>
			</form:form>
		</div>
	
	</div>

</body>
</html>