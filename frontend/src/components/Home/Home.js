import React, {Component} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCaretDown, faCarrot} from "@fortawesome/free-solid-svg-icons";
import {Button, Col, Container, Row} from "react-bootstrap";

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: props.isAuthenticated,
            username: props.username,
            showInstructions: false
        }
    }

    toggleShowInstructions = () => {
        this.setState(prevState => {
            return {showInstructions: !prevState.showInstructions}
        })
    }

    render() {
        const showInstructions = this.state.showInstructions;
        const isAuthenticated = this.state.isAuthenticated;
        const welcomeMessage = "Hi, " + this.state.username + "!";
        return (
            <div>
                {!isAuthenticated ?

                    <div>
                        <header className="header">
                            <h1>Kitchen app</h1>
                            <FontAwesomeIcon icon={faCarrot}/>
                        </header>
                        <p className="green-text">
                            An app to help you remember about that cabbage at the back of the fridge.
                        </p>
                        <p>
                            Login or register to proceed.
                        </p>


                    </div>

                    :

                    <Container>
                        <header className="header">
                            <FontAwesomeIcon icon={faCarrot}/>
                            <p>{welcomeMessage}</p>
                            <Button variant="outline-light" onClick={this.toggleShowInstructions}>
                                Usage instructions <FontAwesomeIcon icon={faCaretDown}/>
                            </Button>
                        </header>

                        {showInstructions &&
                        <Row>
                            <Col sm>
                                <header className="subheader">My ingredients</header>
                                <p className="justified">
                                    This should be a reflection of what you have in your kitchen.
                                    You can search for ingredients and add them to your stock.
                                    If you can't find something, you will be given a chance to create the ingredient.
                                </p>

                            </Col>
                            <Col sm>
                                <header className="subheader">Recipes</header>
                                <p className="justified">
                                    Once you have your ingredients entered, you can see recipes you can make out of what
                                    you have.
                                    Each recipe shows you what ingredients you have and what's missing. <br/>
                                    If you found a recipe you want to make, you can add ingredients to your shopping
                                    list
                                    with just one click of a button.<br/>
                                    If you've found a recipe and cooked it, you can click the make recipe button that
                                    will remove
                                    the ingredients used from your kitchen stock.<br/>
                                    Also, you can also create your own recipes which other users will be able to
                                    see! <br/>
                                </p>
                            </Col>
                            <Col sm>
                                <header className="subheader">Shopping list</header>
                                <p className="justified">

                                    This is where you build your shopping list.
                                    You can add items manually or from recipes as mentioned earlier.
                                    When you're in the shop, just tick off the items that you picked up.
                                    When you're done shopping, click the button to import all the ingredients that
                                    you've picked up to your fridge.
                                </p>
                            </Col>
                        </Row>
                        }

                    </Container>
                }
            </div>

        );
    }
}

export default Home;

