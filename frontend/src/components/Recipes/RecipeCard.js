import {Button, Card, CardImg, Col, Container, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import React from "react";
import {faCaretDown, faCaretUp, faCheck, faTimes} from "@fortawesome/free-solid-svg-icons";
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
            ingredients: props.ingredients,
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

        const ownedIngredients = ingredients.filter(i => i.ownedQuantity > 0).length;
        const ownedIngredientsFullAmount = ingredients.filter(i => i.recipeQuantity <= i.ownedQuantity).length;
        const totalIngredients = ingredients.length;

        const hasFullIngredients = totalIngredients === ownedIngredientsFullAmount;
        const hasPartialIngredients = totalIngredients === ownedIngredients;

        const hasIngredientsIcon = hasFullIngredients ? faCheck : faTimes;
        const partialIngredientsText = hasPartialIngredients && !hasFullIngredients ? ", but not enough of some ingredients " : ""
        const displayIngredients = this.state.displayIngredients;
        const displayIngredientsToggle = displayIngredients ? faCaretUp : faCaretDown;

        const image = this.state.imageLink;
        return (
            <Card>
                {image &&
                <CardImg variant="top" src={image} alt={altText}/>
                }
                <Card.Body>
                    <Card.Title>{this.state.title}</Card.Title>
                </Card.Body>

                <ListGroup className="list-group-flush">
                    <ListGroupItem>
                        <Container>
                            <Row>
                                <Col>
                                    <FontAwesomeIcon icon={hasIngredientsIcon}/>
                                    You have {ownedIngredients}/{totalIngredients} ingredients
                                    to make this recipe{partialIngredientsText}
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
                        <RecipeCardIngredient key={ingredient.ingredientId}
                                              name={ingredient.ingredientName} measurement={ingredient.measurement}
                                              recipeQuantity={ingredient.recipeQuantity}
                                              ownedQuantity={ingredient.ownedQuantity}/>
                    )
                    }

                </ListGroup>

                {displayIngredients &&
                <Card.Footer>
                    <Row>
                        <Col>
                            <Button onClick={() => this.props.handleViewRecipe()}>See full recipe</Button>

                        </Col>
                    </Row>

                </Card.Footer>}
            </Card>
        )
    }
}

export default RecipeCard;