------------------------------------------------------------
-- FABRICANTES
------------------------------------------------------------
INSERT INTO manufacturers (id, name, email, cpnj, country)
VALUES 
    (gen_random_uuid(), 'NVIDIA Corporation', 'contact@nvidia.com', '12.345.678/0001-99', 'USA'),
    (gen_random_uuid(), 'AMD Technologies', 'info@amd.com', '98.765.432/0001-88', 'USA'),
    (gen_random_uuid(), 'Intel Graphics', 'support@intel.com', '11.222.333/0001-00', 'USA');

------------------------------------------------------------
-- MODELOS
------------------------------------------------------------
INSERT INTO models (id, name, release_year, manufacturer_id)
VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'GeForce RTX 4090', 2022,
        (SELECT id FROM manufacturers WHERE name='NVIDIA Corporation')),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Radeon RX 7900 XTX', 2023,
        (SELECT id FROM manufacturers WHERE name='AMD Technologies')),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Arc A770', 2022,
        (SELECT id FROM manufacturers WHERE name='Intel Graphics'));

------------------------------------------------------------
-- CATEGORIAS
------------------------------------------------------------
INSERT INTO categories (id, name, description)
VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'High-End', 'GPUs de alto desempenho voltadas para gamers e criadores.'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Mid-Range', 'GPUs intermediárias com ótimo custo-benefício.'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Entry-Level', 'GPUs básicas para uso cotidiano e leve.');

------------------------------------------------------------
-- TECNOLOGIAS
------------------------------------------------------------
INSERT INTO technologies (id, name, description)
VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Ray Tracing', 'Renderização realista de luz e reflexos.'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'DLSS 3', 'Upscaling baseado em IA para desempenho otimizado.'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'FidelityFX', 'Tecnologia de aprimoramento de imagem da AMD.'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'XeSS', 'Escalonamento baseado em IA da Intel.');

------------------------------------------------------------
-- GPUS (UUID)
------------------------------------------------------------
INSERT INTO gpus (id, name, description, price, is_active, available_quantity,
                  memory, architecture, energy_consumption, model_id)
VALUES 
    ('11111111-1111-1111-1111-111111111111',
     'NVIDIA RTX 4090 Founders Edition',
     'A GPU mais poderosa da NVIDIA, ideal para 4K e IA.',
     15999.90, TRUE, 15, 24, 'Ada Lovelace', 450,
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),

    ('22222222-2222-2222-2222-222222222222',
     'AMD Radeon RX 7900 XTX',
     'Alta performance para 4K com eficiência superior.',
     8999.99, TRUE, 25, 24, 'RDNA 3', 355,
     'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),

    ('33333333-3333-3333-3333-333333333333',
     'Intel Arc A770 16GB',
     'Boa performance por preço competitivo.',
     2499.99, TRUE, 40, 16, 'Xe HPG', 225,
     'cccccccc-cccc-cccc-cccc-cccccccccccc');

------------------------------------------------------------
-- IMAGENS
------------------------------------------------------------
INSERT INTO images (id, url, alt_text, gpu_id, object_name) VALUES
    (gen_random_uuid(),
     'https://m.media-amazon.com/images/I/71BdePK7bWL._AC_SX679_.jpg',
     'RTX 4090 - frente',
     '11111111-1111-1111-1111-111111111111',
     'gpus/11111111-1111-1111-1111-111111111111/71BdePK7bWL._AC_SX679_.jpg'),
    (gen_random_uuid(),
     'https://m.media-amazon.com/images/I/71BdePK7bWL._AC_SX679_.jpg',
     'RTX 4090 - traseira',
     '11111111-1111-1111-1111-111111111111',
     'gpus/11111111-1111-1111-1111-111111111111/71BdePK7bWL._AC_SX679_.jpg'),
    (gen_random_uuid(),
     'https://br.octoshop.com/cdn/shop/files/RX7900XTX-O24G-B_0.jpg?v=1739307638&width=1220',
     'RX 7900 XTX - frente',
     '22222222-2222-2222-2222-222222222222',
     'gpus/22222222-2222-2222-2222-222222222222/RX7900XTX-O24G-B_0.jpg'),
    (gen_random_uuid(),
     'https://cdn.awsli.com.br/2500x2500/2508/2508057/produto/197525754/1-42e15991ed.jpg',
     'Arc A770 - frente',
     '33333333-3333-3333-3333-333333333333',
     'gpus/33333333-3333-3333-3333-333333333333/1-42e15991ed.jpg');

------------------------------------------------------------
-- RELACIONAMENTOS GPU x TECNOLOGIA
------------------------------------------------------------
INSERT INTO gpu_technologies (gpu_id, technology_id)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),

    ('22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),

    ('33333333-3333-3333-3333-333333333333', 'dddddddd-dddd-dddd-dddd-dddddddddddd');

------------------------------------------------------------
-- RELACIONAMENTOS GPU x CATEGORIA
------------------------------------------------------------
INSERT INTO gpu_categories (gpu_id, category_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

------------------------------------------------------------
-- USUÁRIOS
------------------------------------------------------------
INSERT INTO users (id, name, email, phone_number, cpf, password, role, is_active)
VALUES
    (gen_random_uuid(), 'Arthur Schneider', 'arthur@magistrar.com', '+55 63 99999-0001', '12345678900', 'hashed123', 'ADMIN', TRUE),
    (gen_random_uuid(), 'João Pereira', 'joao@gmail.com', '+55 63 98888-1111', '98765432100', 'hashed456', 'CUSTOMER', TRUE),
    (gen_random_uuid(), 'Maria Silva', 'maria@gmail.com', '+55 63 97777-2222', '19283746500', 'hashed789', 'CUSTOMER', TRUE);

------------------------------------------------------------
-- ENDEREÇOS
------------------------------------------------------------
INSERT INTO addresses (id, street, city, state, zip_code, country, user_id)
VALUES
    (gen_random_uuid(), 'Av. JK, 1234', 'Palmas', 'TO', '77000-000', 'Brasil',
        (SELECT id FROM users WHERE email='joao@gmail.com')),

    (gen_random_uuid(), 'Rua das Flores, 98', 'Gurupi', 'TO', '77400-000', 'Brasil',
        (SELECT id FROM users WHERE email='maria@gmail.com'));

------------------------------------------------------------
-- TRANSAÇÕES DE ESTOQUE
------------------------------------------------------------
INSERT INTO inventory_transactions (id, gpu_id, quantity, transaction_date, reason, transaction_type)
VALUES
    (gen_random_uuid(), '11111111-1111-1111-1111-111111111111', 5, '2025-10-01T10:00:00', 'Reabastecimento de estoque', 'ADD'),
    (gen_random_uuid(), '11111111-1111-1111-1111-111111111111', 2, '2025-10-05T15:30:00', 'Venda de duas unidades', 'REMOVE'),
    (gen_random_uuid(), '22222222-2222-2222-2222-222222222222', 10, '2025-10-02T09:00:00', 'Recebimento de remessa', 'ADD'),
    (gen_random_uuid(), '33333333-3333-3333-3333-333333333333', 40, '2025-10-03T12:00:00', 'Primeiro lote recebido', 'ADD');
