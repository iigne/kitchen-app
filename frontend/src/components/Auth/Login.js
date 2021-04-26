import React, {Component} from "react";
import {Button, Container, Form} from "react-bootstrap";
import {login} from "../../api/Api";

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: ""
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    handleSubmit = (e) => {
        e.preventDefault()
        const credentials = {
            username: this.state.username,
            password: this.state.password
        }
        login(credentials, res => {
                if (res.data.token) {
                    this.props.handleLogin(res.data);
                }
            }, () => {
                this.props.showAlert("Login failed, please check your credentials", "error");
            }
        );
    }

    render() {

        return (
            <Container>
                <h2>Login</h2>
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group controlId="formUsername">
                        <Form.Label>Username</Form.Label>
                        <Form.Control name="username" type="text" value={this.state.username}
                                      onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control name="password" type="password" value={this.state.password}
                                      onChange={this.handleChange}/>
                    </Form.Group>

                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </Container>
        )
    }
}

export default Login;