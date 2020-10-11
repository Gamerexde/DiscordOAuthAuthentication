import {Nav, Navbar, Image, NavDropdown} from "react-bootstrap";

const discordurl = "https://discord.com/api/oauth2/authorize?client_id=762340445388406805&redirect_uri=http%3A%2F%2F127.0.0.1%3A3000%2Fverify&response_type=code&scope=identify"

class NavBar extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div>
                <Navbar bg="light" expand="lg">
                    <Navbar.Brand href="#home">Discord OAuth</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            <Nav.Link href="/">Home</Nav.Link>
                        </Nav>
                        <Nav>
                            <NavCollapse info={this.props.userinfo} />
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            </div>
        )
    }
}

function NavCollapse(props) {
    if (props.info) {
        return (
            <Navbar.Text>
                Signed in as: <a href="#login">{props.info.username}</a>
                <Image src={avatar(props.info.id, props.info.avatar)} roundedCircle />
            </Navbar.Text>
        )
    } else {
        return (
            <Nav.Link href={discordurl}>Login</Nav.Link>
        )
    }
}

function avatar(userid, avatarid) {
    let url = "https://cdn.discordapp.com/avatars/" + userid + "/" + avatarid + ".png?size=32"
    return url;
}

export default NavBar;