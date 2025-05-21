import React from 'react';
import { Container, Navbar } from 'react-bootstrap';
import Logo from './Logo';
import SearchBar from './SearchBar';
import AuthButtons from './AuthButtons';

// 関数: MainNavbar
const MainNavbar = ({ searchQuery }) => {
  return (
    <Navbar collapseOnSelect bg="dark" expand="md" fixed="top" className="border-bottom">
      <Container fluid="md">
        <Logo />
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <AuthButtons />
          <SearchBar keyword={searchQuery} />
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default MainNavbar;
