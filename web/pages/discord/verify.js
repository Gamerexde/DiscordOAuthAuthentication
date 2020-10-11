import cookies from "next-cookies";
import axios from "axios";
import NavBar from "../../components/navbar";
import {Card, Container, Button} from "react-bootstrap";
import Loader from 'react-loader-spinner'

class VerifyUser extends React.Component {
    static async getInitialProps(ctx) {
        let token = cookies(ctx).access_token
        let userInfo = null;
        await axios.get('http://127.0.0.1:4000/user/info', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        ).then(response => { userInfo = response.data })

        return {
            user: userInfo,
            token: cookies(ctx).access_token
        }
    }
    constructor() {
        super();

        this.state = {
            status: "normal"
        }
        this.verifyAccept = this.verifyAccept.bind(this);
    }

    verifyAccept() {
        this.setState({status: "loading"})

        let token = this.props.token
        console.log(token)

        axios.get('http://localhost:4000/discord/verify', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }).then(response => {
            switch(response.data.status) {
                case "ok":
                    this.setState({status: "done"})
                    break;
                case "already":
                    this.setState({status: "already"})
                    break;
            }
        }).catch(error => {})
    }



    render() {
        return (
            <div>
                <NavBar userinfo={this.props.user}/>
                <Container>
                    <br />
                    <Card className="text-center" style={ {border: '0px solid rgba(0,0,0,.125)'}}>
                        <CardBody inst={this} />
                    </Card>
                </Container>
            </div>
        )
    }
}

function CardBody(props) {
    let instance = props.inst
    switch(instance.state.status) {
        case "normal":
            return (
                <Card.Body>
                    <br />
                    <h3>You will be verifying youself as: {instance.props.user.username}#{instance.props.user.discriminator}</h3>
                    <p>Is this correct?</p>
                    <Button variant="danger">No</Button> {' '} <Button onClick={instance.verifyAccept} variant="success">Yes</Button>

                </Card.Body>
            )
        case "loading":
            return (
                <Card.Body>
                    <br />
                    <Loader
                        type="Puff"
                        color="#00BFFF"
                        height={100}
                        width={100}
                        timeout={3000} //3 secs
                    />

                </Card.Body>
            )
        case "done":
            return (
                <Card.Body>
                    <br />
                    <h3>You have been verified!</h3>
                </Card.Body>
            )
        case "already":
            return (
                <Card.Body>
                    <br />
                    <h3>You are already verified!</h3>
                </Card.Body>
            )
    }

}
export default VerifyUser;