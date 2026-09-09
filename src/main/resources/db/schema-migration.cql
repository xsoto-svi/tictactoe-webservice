CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_room_table (
    room_code text,
    game_id uuid,
    PRIMARY KEY (room_code, game_id)
);

CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_player_table (
    player_name text,
    game_id uuid,
    PRIMARY KEY (player_name, game_id)
);

CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_game_table (
    game_id uuid,
    player_name text,
    symbol text,
    location int,
    date_save timestamp,
    PRIMARY KEY (game_id, date_save)
) WITH CLUSTERING ORDER BY (date_save ASC);

CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_pending_game_table (
    room_code text,
    game_id uuid,
    player_name text,
    PRIMARY KEY (room_code, game_id)
);