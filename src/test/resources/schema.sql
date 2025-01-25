CREATE TABLE vendedor (
                          id UUID PRIMARY KEY,
                          email VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          vendedor_nome VARCHAR(45) NOT NULL,
                          vendedor_setor VARCHAR(45) NOT NULL,
                          roles VARCHAR(255)
);

CREATE TABLE produto (
                         id UUID PRIMARY KEY,
                         produto_descricao VARCHAR(45) NOT NULL,
                         produto_valor DECIMAL(7,2) NOT NULL,
                         produto_data_hora_saida TIMESTAMP NOT NULL,
                         quantidade_estoque INT NOT NULL,
                         vendedor_id UUID NOT NULL,
                         FOREIGN KEY (vendedor_id) REFERENCES vendedor(id)
);

CREATE TABLE pedido (
                        id UUID PRIMARY KEY,
                        pedido_descricao VARCHAR(45) NOT NULL,
                        pedido_valor DECIMAL(7,2) NOT NULL,
                        pedido_data_hora TIMESTAMP NOT NULL,
                        pedido_quantidade INT NOT NULL,
                        vendedor_id UUID NOT NULL,
                        FOREIGN KEY (vendedor_id) REFERENCES vendedor(id)
);

CREATE TABLE pedido_tem_produtos (
                                     id UUID PRIMARY KEY,
                                     pedido_id UUID NOT NULL,
                                     produto_id UUID NOT NULL,
                                     quantidade INT NOT NULL,
                                     preco_unitario DECIMAL(7,2) NOT NULL,
                                     FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                                     FOREIGN KEY (produto_id) REFERENCES produto(id)
);
