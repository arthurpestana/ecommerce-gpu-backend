------------------------------------------------------------
-- FABRICANTES
------------------------------------------------------------
INSERT INTO manufacturers (name, email, cpnj, country)
VALUES 
    ('NVIDIA Corporation', 'contact@nvidia.com', '12.345.678/0001-99', 'USA'),
    ('AMD Technologies', 'info@amd.com', '98.765.432/0001-88', 'USA'),
    ('Intel Graphics', 'support@intel.com', '11.222.333/0001-00', 'USA');

------------------------------------------------------------
-- MODELOS
------------------------------------------------------------
INSERT INTO models (name, release_year, manufacturer_id)
VALUES 
    ('GeForce RTX 4090', 2022, 1),
    ('Radeon RX 7900 XTX', 2023, 2),
    ('Arc A770', 2022, 3);

------------------------------------------------------------
-- CATEGORIAS
------------------------------------------------------------
INSERT INTO categories (name, description)
VALUES 
    ('High-End', 'GPUs de alto desempenho voltadas para gamers e criadores.'),
    ('Mid-Range', 'GPUs intermediárias com ótimo custo-benefício.'),
    ('Entry-Level', 'GPUs básicas para uso cotidiano e leve.');

------------------------------------------------------------
-- TECNOLOGIAS
------------------------------------------------------------
INSERT INTO technologies (name, description)
VALUES 
    ('Ray Tracing', 'Renderização realista de luz e reflexos.'),
    ('DLSS 3', 'Upscaling baseado em IA para desempenho otimizado.'),
    ('FidelityFX', 'Tecnologia de aprimoramento de imagem da AMD.'),
    ('XeSS', 'Escalonamento baseado em IA da Intel.');

------------------------------------------------------------
-- GPUS
------------------------------------------------------------
INSERT INTO gpus (name, description, price, is_active, available_quantity, memory, architecture, energy_consumption, model_id)
VALUES 
    ('NVIDIA RTX 4090 Founders Edition', 'A GPU mais poderosa da NVIDIA, ideal para 4K e IA.', 15999.90, TRUE, 15, 24, 'Ada Lovelace', 450, 1),
    ('AMD Radeon RX 7900 XTX', 'Alta performance para 4K com eficiência superior.', 8999.99, TRUE, 25, 24, 'RDNA 3', 355, 2),
    ('Intel Arc A770 16GB', 'Boa performance por preço competitivo.', 2499.99, TRUE, 40, 16, 'Xe HPG', 225, 3);

------------------------------------------------------------
-- IMAGENS
------------------------------------------------------------
INSERT INTO images (url, alt_text, gpu_id)
VALUES
    ('https://cdn.gpus.com/images/rtx4090-front.jpg', 'RTX 4090 - frente', 1),
    ('https://cdn.gpus.com/images/rtx4090-back.jpg', 'RTX 4090 - traseira', 1),
    ('https://cdn.gpus.com/images/rx7900-front.jpg', 'RX 7900 XTX - frente', 2),
    ('https://cdn.gpus.com/images/a770-front.jpg', 'Arc A770 - frente', 3);

------------------------------------------------------------
-- RELACIONAMENTOS GPU x TECNOLOGIA
------------------------------------------------------------
INSERT INTO gpu_technologies (gpu_id, technology_id)
VALUES 
    (1, 1), (1, 2),        -- RTX 4090 -> Ray Tracing + DLSS
    (2, 1), (2, 3),        -- RX 7900 XTX -> Ray Tracing + FidelityFX
    (3, 4);                -- Arc A770 -> XeSS

------------------------------------------------------------
-- RELACIONAMENTOS GPU x CATEGORIA
------------------------------------------------------------
INSERT INTO gpu_categories (gpu_id, category_id)
VALUES
    (1, 1), -- RTX 4090 -> High-End
    (2, 2), -- RX 7900 XTX -> Mid-Range
    (3, 3); -- Arc A770 -> Entry-Level

------------------------------------------------------------
-- USUÁRIOS
------------------------------------------------------------
INSERT INTO users (name, email, phone_number, cpf, password, role, is_active)
VALUES
    ('Arthur Schneider', 'arthur@magistrar.com', '+55 63 99999-0001', '12345678900', 'hashed123', 'ADMIN', TRUE),
    ('João Pereira', 'joao@gmail.com', '+55 63 98888-1111', '98765432100', 'hashed456', 'CUSTOMER', TRUE),
    ('Maria Silva', 'maria@gmail.com', '+55 63 97777-2222', '19283746500', 'hashed789', 'CUSTOMER', TRUE);

------------------------------------------------------------
-- ENDEREÇOS
------------------------------------------------------------
INSERT INTO addresses (street, city, state, zip_code, country, user_id)
VALUES
    ('Av. JK, 1234', 'Palmas', 'TO', '77000-000', 'Brasil', 2),
    ('Rua das Flores, 98', 'Gurupi', 'TO', '77400-000', 'Brasil', 3);

------------------------------------------------------------
-- TRANSAÇÕES DE ESTOQUE
------------------------------------------------------------
INSERT INTO inventory_transactions (gpu_id, quantity, transaction_date, reason, transaction_type)
VALUES
    (1, 5, '2025-10-01T10:00:00', 'Reabastecimento de estoque', 'ADD'),
    (1, 2, '2025-10-05T15:30:00', 'Venda de duas unidades', 'REMOVE'),
    (2, 10, '2025-10-02T09:00:00', 'Recebimento de remessa', 'ADD'),
    (3, 40, '2025-10-03T12:00:00', 'Primeiro lote recebido', 'ADD');
