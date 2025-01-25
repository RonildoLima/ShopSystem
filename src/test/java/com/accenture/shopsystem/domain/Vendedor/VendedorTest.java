package com.accenture.shopsystem.domain.Vendedor;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendedorTest {

    @Test
    void getEmail() {
        Vendedor vendedor = new Vendedor();
        vendedor.setEmail("vendedor@test.com");
        assertEquals("vendedor@test.com", vendedor.getEmail());
    }

    @Test
    void setEmail() {
        Vendedor vendedor = new Vendedor();
        vendedor.setEmail("novoemail@test.com");
        assertEquals("novoemail@test.com", vendedor.getEmail());
    }

    @Test
    void getNome() {
        Vendedor vendedor = new Vendedor();
        vendedor.setVendedorNome("João Silva");
        assertEquals("João Silva", vendedor.getVendedorNome());
    }

    @Test
    void setNome() {
        Vendedor vendedor = new Vendedor();
        vendedor.setVendedorNome("Maria Oliveira");
        assertEquals("Maria Oliveira", vendedor.getVendedorNome());
    }

    @Test
    void getSenha() {
        Vendedor vendedor = new Vendedor();
        vendedor.setPassword("senha123");
        assertEquals("senha123", vendedor.getPassword());
    }

    @Test
    void setSenha() {
        Vendedor vendedor = new Vendedor();
        vendedor.setPassword("novaSenha123");
        assertEquals("novaSenha123", vendedor.getPassword());
    }

    @Test
    void testEquals() {
        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId("1");
        vendedor1.setEmail("vendedor@test.com");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId("1");
        vendedor2.setEmail("vendedor@test.com");

        assertEquals(vendedor1, vendedor2);

        Vendedor vendedor3 = new Vendedor();
        vendedor3.setId("2");
        vendedor3.setEmail("outroemail@test.com");

        assertNotEquals(vendedor1, vendedor3);
    }

    @Test
    void testHashCode() {
        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId("1");
        vendedor1.setEmail("vendedor@test.com");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId("1");
        vendedor2.setEmail("vendedor@test.com");

        assertEquals(vendedor1.hashCode(), vendedor2.hashCode());

        Vendedor vendedor3 = new Vendedor();
        vendedor3.setId("2");
        vendedor3.setEmail("outroemail@test.com");

        assertNotEquals(vendedor1.hashCode(), vendedor3.hashCode());
    }

    @Test
    void testToString() {
        Vendedor vendedor = new Vendedor();
        vendedor.setId("1");
        vendedor.setVendedorNome("João Silva");
        vendedor.setVendedorSetor("Eletrônicos");

        String expected = "Vendedor{idVendedor='1', vendedorNome='João Silva', vendedorSetor='Eletrônicos'}";
        assertEquals(expected, vendedor.toString());
    }
}
