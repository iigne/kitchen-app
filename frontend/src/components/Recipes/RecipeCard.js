import {Button, Card, CardImg, Col, Container, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import React from "react";
import {
    faCaretDown,
    faCaretUp,
    faCheck,
    // faShoppingBasket,
    // faShoppingCart,
    faTimes
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import RecipeCardIngredient from "./RecipeCardIngredient";

class RecipeCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            title: props.title,
            imageLink: props.imageLink,
            method: props.method,
            ingredients: props.recipeIngredients,
            ownedIngredients: [],
            displayIngredients: false
        }
    }

    toggleDisplayIngredients = () => {
        this.setState(prevState => {
            return {displayIngredients: !prevState.displayIngredients}
        })
    }

    render() {
        const altText = "image of " + this.state.title;
        const ingredients = this.state.ingredients;
        const ownedIngredients = this.state.ownedIngredients;
        const hasIngredients = ingredients.length === ownedIngredients.length;
        const hasIngredientsIcon = hasIngredients ? faCheck : faTimes;
        let displayIngredients = this.state.displayIngredients;
        let displayIngredientsToggle = displayIngredients ? faCaretUp : faCaretDown;
        return (
            <Card>
                <CardImg variant="top" src={this.state.imageLink} alt={altText}/>
                <Card.Body>
                    <Card.Title>{this.state.title}</Card.Title>
                </Card.Body>
                <ListGroup className="list-group-flush">
                    <ListGroupItem>
                        <Container>
                            <Row>
                                <Col>
                                    <FontAwesomeIcon icon={hasIngredientsIcon}/> You have {ownedIngredients.length} out
                                    of {ingredients.length} ingredients to make this recipe
                                </Col>
                                <Col xs={1}>
                                    <Button size="sm" variant="light" onClick={this.toggleDisplayIngredients}>
                                        <FontAwesomeIcon icon={displayIngredientsToggle}/>
                                    </Button>
                                </Col>
                            </Row>
                        </Container>

                    </ListGroupItem>
                    {displayIngredients &&
                    ingredients.map((ingredient) =>
                        <RecipeCardIngredient name={ingredient.ingredientName} measurement={ingredient.measurement}
                                              recipeQuantity={ingredient.quantity} />
                    )
                    }
                    {/*<ListGroupItem variant="success">This recipe uses up some ingredient that you should use up within this week</ListGroupItem>*/}

                </ListGroup>

                {displayIngredients &&
                <Card.Footer>
                    <Row>
                        <Col>
                            <Button onClick={() => this.props.handleViewRecipe()}>See full recipe</Button>

                        </Col>
                        {/*TODO implement someday*/}
                        {/*{!hasIngredients &&*/}
                        {/*<Col>*/}
                        {/*    Add missing ingredients to shopping list*/}

                        {/*    <Button>*/}
                        {/*        <FontAwesomeIcon icon={faShoppingCart}/>*/}
                        {/*    </Button>*/}
                        {/*</Col>*/}
                        {/*}*/}

                    </Row>

                </Card.Footer>}
            </Card>
        )
    }
}

export default RecipeCard;