package com.fintech.app.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUsuario {

    @Test
    public void TestCrearUsuarioValido(){
        Usuario usuario = new Usuario(1, "Nombre", "Apellido", "email@example.com", "Password1!", "12345678");

        assertEquals(1, usuario.getUsuarioID());
        assertEquals("Nombre", usuario.getNombre());
        assertEquals("Apellido", usuario.getApellido());
        assertEquals("email@example.com", usuario.getEmail());
        assertEquals("Password1!", usuario.getPassword());
        assertEquals("12345678", usuario.getDNI());
    }

    @Test
    public void TestConstructorRechazaNulls(){
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, null, "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", null, "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", null, "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "email@example.com", null, "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "email@example.com", "Password1!", null));
    }

    @Test
    public void TestConstructorRechazaBlancos(){
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "", "Apellido", "email@example.com", "Password1!", " "));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "email@example.com", "", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "", "", "", "", ""));
    }

    @Test
    public void TestConstructorRechazaIDInvalido(){
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(-1, "Nombre", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(0, "Nombre", "Apellido", "email@example.com", "Password1!", "12345678"));
    }

    @Test
    public void TestConstructorRechazaNombreInvalido(){
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "A", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ar", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari12", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari!@0_", "Apellido", "email@example.com", "Password1!", "12345678"));
        assertDoesNotThrow(() ->  new Usuario(1, "Arimedes", "Apellido", "email@example.com", "Password1!", "12345678"));
    }

    @Test
    public void TestContructorRechazaApellidoInvalido(){
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "", "", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, null, null, "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "A", "A", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ar", "Ap", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari", "Ape", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari12", "Ape12", "email@example.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Ari!@0_", "Ape!@0_", "email@example.com", "Password1!", "12345678"));
        assertDoesNotThrow(() ->  new Usuario(1, "Arimedes", "Apellido", "email@example.com", "Password1!", "12345678"));
    }

    @Test
    public void TestConstructorRechazaEmailInvalido(){
        assertDoesNotThrow(() ->  new Usuario(1, "Arimedes", "Apellido", "user@example.com", "Password1!", "12345678"));
        assertDoesNotThrow(() ->  new Usuario(1, "Arimedes", "Apellido", "user.name+tag@sub.domain.co", "Password1!", "12345678"));
        assertDoesNotThrow(() ->  new Usuario(1, "Arimedes", "Apellido", "u@a.io", "Password1!", "12345678"));

        // inválidos
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "sin-arroba.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "a@b", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "@dominio.com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "user@dominio..com", "Password1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () ->  new Usuario(1, "Nombre", "Apellido", "user dominio@com", "Password1!", "12345678"));
    }

    @Test
    public void TestConstructorRechazaPasswordInvalido(){
        assertDoesNotThrow(() -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12345678"));
        assertDoesNotThrow(() -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "Zz9#xxxxx", "12345678"));
        assertDoesNotThrow(() -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "S3guro!2024", "12345678"));

        // inválidos
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "corta1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "SOLOMAYUS1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "solominus1!", "12345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "SinNumero!!", "12345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "SinSimbolo12", "12345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "Con espacio1!", "12345678"));
    }

    @Test
    public void TestConstructorRechazaDNIInvalido(){
        assertDoesNotThrow(() -> new Usuario(1, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12345678"));
        assertDoesNotThrow(() -> new Usuario(2, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "00000000"));
        assertDoesNotThrow(() -> new Usuario(3, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "00012345"));
        assertDoesNotThrow(() -> new Usuario(4, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "87654321"));
        assertDoesNotThrow(() -> new Usuario(5, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "11111111"));
        assertDoesNotThrow(() -> new Usuario(6, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "22222222"));

        // inválidos
        assertThrows(IllegalArgumentException.class, () -> new Usuario(7,  "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", ""));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(8,  "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "1"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(9,  "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "1234567"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(10, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "123456789"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(11, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12.345.678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(12, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12 345 678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(13, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "A2345678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(14, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "1234-678"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(15, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "\t1234567"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(16, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "1234567\n"));
        assertThrows(IllegalArgumentException.class, () -> new Usuario(17, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "１２３４５６７８"));
    }

    @Test
    public void TestDosUsuariosSonIguales(){
        Usuario usuario =  new Usuario(1, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12345678");
        Usuario usuario2 =  new Usuario(1, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12345678");

        assertEquals(usuario, usuario2, "equals considera identidad a mismo usuarioID, mismo Email, mismo DNI");
        assertEquals(usuario.hashCode(), usuario2.hashCode(), "hashCode coherente con equals");
    }

    @Test
    void toStringNoExponePassword() {
        Usuario u = new Usuario(1, "Nombre", "Apellido", "email@example.com", "Aa1!aaaa", "12345678");
        String s = u.toString().toLowerCase();
        assertFalse(s.contains("aa1!aaaa"), "No debe mostrar el valor de password");
        assertFalse(s.contains("password"), "Evitar incluso la etiqueta 'password' en claro");
    }
}
