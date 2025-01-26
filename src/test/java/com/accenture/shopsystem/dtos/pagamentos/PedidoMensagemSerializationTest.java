package com.accenture.shopsystem.dtos.pagamentos;

import org.junit.jupiter.api.Test;

import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.Arrays;

public class PedidoMensagemSerializationTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        // Criando a instância de PedidoMensagem
        PedidoMensagem pedido = new PedidoMensagem();
        pedido.setPedidoId(1L);
        pedido.setCliente("Carlos");
        pedido.setProdutosIds(Arrays.asList(1L, 2L, 3L));
        pedido.setValorTotal(200.00);
        pedido.setStatus("PENDENTE");

        // Serializando o objeto
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(pedido);
        objectOutputStream.flush();
        byte[] data = byteArrayOutputStream.toByteArray();

        // Desserializando o objeto
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        PedidoMensagem deserializedPedido = (PedidoMensagem) objectInputStream.readObject();

        // Verificando se a desserialização preserva os dados
        assertEquals(pedido.getPedidoId(), deserializedPedido.getPedidoId());
        assertEquals(pedido.getCliente(), deserializedPedido.getCliente());
        assertEquals(pedido.getProdutosIds(), deserializedPedido.getProdutosIds());
        assertEquals(pedido.getValorTotal(), deserializedPedido.getValorTotal());
        assertEquals(pedido.getStatus(), deserializedPedido.getStatus());
    }
}