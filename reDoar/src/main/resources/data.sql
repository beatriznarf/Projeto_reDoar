INSERT INTO usuarios (id, email, senha, cargo, cpf, nome, data_cadastro)
SELECT '1', 'admin@gmail.com', 'admin', 'administrador', '111111', 'admin', CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = '1');