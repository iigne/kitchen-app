import React from 'react'
import { Redirect } from 'react-router-dom'

class PrivateRoute extends React.Component {

    render() {
        const Component = this.props.component;
        const isAuthenticated = this.props.isAuthenticated;

        return isAuthenticated ? (<Component />) : (<Redirect to={{ pathname: '/login' }} />);
    }
}

export default PrivateRoute;

