import Router from 'next/router'

import NavBar from "../components/navbar";
import {Card, Container, Button} from "react-bootstrap";
import axios from 'axios'
import cookies from 'next-cookies'

const discordurl = "https://discord.com/api/oauth2/authorize?client_id=762340445388406805&redirect_uri=http%3A%2F%2F127.0.0.1%3A3000%2Fverify&response_type=code&scope=identify"

class Verify extends React.Component {
    static getInitialProps(ctx) {
        let query = ctx.query
        return {
            query: query,
            token: cookies(ctx).access_token
        }
    }

    constructor() {
        super();
        this.state = {
            authState: "normal"
        }

    }

    componentDidMount() {
        if (this.props.token) {
            this.setState({authState: "success"})
            return;
        }
        if (this.props.query.code) {
            console.log("code exist")
            axios.get('http://127.0.0.1:4000/verify?code=' + this.props.query.code, {withCredentials: true})
                .then(response => {
                    console.log(response)
                    this.setState({authState: "success"})
                }).catch(error => {
                this.setState({authState: "error"})
            })
        }
    }


    render() {
        return (
            <div>
                <NavBar />
                <Container>
                    <br />
                    <CardStatus state={this.state}/>
                </Container>
            </div>
        );
    }
}

function CardStatus(props) {
    let authstate = props.state.authState
    switch(authstate) {
        case "success":
            Router.push('/')
            return (
                <Card>
                    <Card.Body>
                        <Card.Title>Logged In!</Card.Title>
                        <Card.Text>You have been logged in! Return to the home.</Card.Text>
                        <Button variant="success" href='/'>Return to Home</Button>
                    </Card.Body>
                </Card>
            )
        case "error":
            return (
                <Card>
                    <Card.Body>
                        <Card.Title>An error ocurred.</Card.Title>
                        <Card.Text>Retry again.</Card.Text>
                        <Button variant="dark" href={discordurl}>Login with Discord</Button>
                    </Card.Body>
                </Card>
            )
        default:
            return (
                <Card>
                    <Card.Body>
                        <Card.Title>Connect with Discord</Card.Title>
                        <Card.Text>In order to authenticate you will need to link your Discord account.</Card.Text>
                        <Button variant="dark" href={discordurl}>Login with Discord</Button>
                    </Card.Body>
                </Card>
            )

    }
}

export default Verify;