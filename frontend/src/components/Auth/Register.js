import React, {Component} from "react";
import {Alert, Button, Container, Form, FormText} from "react-bootstrap";
import {Redirect} from "react-router-dom";
import {register} from "../../api/Api";


class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            email: "",
            password: "",
            confirmPassword: ""
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    handleSubmit = (e) => {
        e.preventDefault()
        if (this.validateForm()) {
            const registerRequest = {
                username: this.state.username,
                email: this.state.email,
                password: this.state.password
            }
            register(registerRequest, () => {
                this.setState({success: true})
                this.props.showAlert("Successfully registered! You can now log in.", "success");
            }, (error) => {
                this.props.showAlert(error.response.data.errorMessage, "error");
            });
        }
    }

    validateForm() {
        if (this.state.username.length === 0) {
            this.props.showAlert("Username must not be blank", "error");
            return false;
        }
        if (this.state.email.length === 0) {
            this.props.showAlert("Email must not be blank", "error");
            return false;
        }
        if (this.state.password !== this.state.confirmPassword) {
            this.props.showAlert("Passwords must match", "error");
            return false;
        }
        return true;
    }

    render() {
        return (
            <Container>
                {this.state.success && <Redirect to="/login"/>}
                <h2>Register</h2>
                <Alert variant="warning">
                    This website is for demonstration purposes only - please use a fake email and avoid entering any
                    personal details
                </Alert>
                <Form onSubmit={this.handleSubmit}
                      hidden={this.state.success != null && this.state.success === true}>
                    <Form.Group controlId="formUsername">
                        <Form.Label>Username</Form.Label>
                        <Form.Control name="username" type="text" value={this.state.username}
                                      onChange={this.handleChange}/>
                        <FormText muted>Username must contain at least 3 characters</FormText>
                    </Form.Group>

                    <Form.Group controlId="formEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control name="email" type="email" value={this.state.email}
                                      onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control name="password" type="password" value={this.state.password}
                                      onChange={this.handleChange}/>
                        <FormText muted>Password must contain at least 6 characters</FormText>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Confirm password</Form.Label>
                        <Form.Control name="confirmPassword" type="password" value={this.state.confirmPassword}
                                      onChange={this.handleChange}/>
                    </Form.Group>

                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </Container>
        );
    }
}

export default Register;