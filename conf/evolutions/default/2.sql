# --- !Ups
ALTER TABLE course ADD COLUMN description VARCHAR(255)

# --- !Downs
ALTER TABLE course DROP description

