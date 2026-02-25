-- ============================================================
-- Base de données : Système de gestion des réparations
-- Projet Supabase · PostgreSQL
-- 18 tables
-- ============================================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- 1. role
-- ============================================================
CREATE TABLE IF NOT EXISTS role (
    id_role   SERIAL PRIMARY KEY,
    nom_role  VARCHAR(50) NOT NULL UNIQUE  -- ex: Technicien, Responsable
);

-- ============================================================
-- 2. boutique
-- ============================================================
CREATE TABLE IF NOT EXISTS boutique (
    id_boutique  SERIAL PRIMARY KEY,
    nom          VARCHAR(100) NOT NULL,
    adresse      TEXT,
    telephone    VARCHAR(20),
    email        VARCHAR(100),
    ville        VARCHAR(100)
);

-- ============================================================
-- 3. employe
-- ============================================================
CREATE TABLE IF NOT EXISTS employe (
    id_employe    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    telephone     VARCHAR(20),
    id_role       INT NOT NULL REFERENCES role(id_role),
    date_embauche DATE,
    actif         BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- 4. employe_boutique  (junction : un employé peut travailler dans plusieurs boutiques)
-- ============================================================
CREATE TABLE IF NOT EXISTS employe_boutique (
    id_employe   UUID NOT NULL REFERENCES employe(id_employe) ON DELETE CASCADE,
    id_boutique  INT  NOT NULL REFERENCES boutique(id_boutique) ON DELETE CASCADE,
    PRIMARY KEY (id_employe, id_boutique)
);

-- ============================================================
-- 5. client
-- ============================================================
CREATE TABLE IF NOT EXISTS client (
    id_client   SERIAL PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    prenom      VARCHAR(100) NOT NULL,
    telephone   VARCHAR(20),
    email       VARCHAR(150),
    ville       VARCHAR(100),
    date_inscription TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 6. type_appareil
-- ============================================================
CREATE TABLE IF NOT EXISTS type_appareil (
    id_type_appareil  SERIAL PRIMARY KEY,
    libelle           VARCHAR(100) NOT NULL UNIQUE  -- ex: Smartphone, Tablette, PC portable
);

-- ============================================================
-- 7. appareil
-- ============================================================
CREATE TABLE IF NOT EXISTS appareil (
    id_appareil       SERIAL PRIMARY KEY,
    id_client         INT NOT NULL REFERENCES client(id_client) ON DELETE CASCADE,
    id_type_appareil  INT NOT NULL REFERENCES type_appareil(id_type_appareil),
    marque            VARCHAR(100),
    modele            VARCHAR(100),
    numero_serie      VARCHAR(100),
    date_achat        DATE
);

-- ============================================================
-- 8. type_reparation
-- ============================================================
CREATE TABLE IF NOT EXISTS type_reparation (
    id_type_reparation  SERIAL PRIMARY KEY,
    libelle             VARCHAR(150) NOT NULL UNIQUE,  -- ex: Remplacement écran, Batterie, Carte mère
    prix_base           NUMERIC(10,2)
);

-- ============================================================
-- 9. reparation
-- ============================================================
CREATE TABLE IF NOT EXISTS reparation (
    id_reparation       SERIAL PRIMARY KEY,
    numero_suivi        VARCHAR(50) NOT NULL UNIQUE,
    id_client           INT  NOT NULL REFERENCES client(id_client),
    id_appareil         INT  NOT NULL REFERENCES appareil(id_appareil),
    id_type_reparation  INT  NOT NULL REFERENCES type_reparation(id_type_reparation),
    id_boutique         INT  NOT NULL REFERENCES boutique(id_boutique),
    id_employe          UUID NOT NULL REFERENCES employe(id_employe),
    description_panne   TEXT,
    statut_actuel       VARCHAR(50) NOT NULL DEFAULT 'Déposé',
                        -- Déposé | Diagnostiqué | En cours | Terminé | Livré | Annulé
    date_depot          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_fin_prevue     DATE,
    date_livraison      TIMESTAMPTZ,
    commentaire_interne TEXT
);

-- ============================================================
-- 10. historique_statut
-- ============================================================
CREATE TABLE IF NOT EXISTS historique_statut (
    id_historique   SERIAL PRIMARY KEY,
    id_reparation   INT  NOT NULL REFERENCES reparation(id_reparation) ON DELETE CASCADE,
    ancien_statut   VARCHAR(50),
    nouveau_statut  VARCHAR(50) NOT NULL,
    commentaire     TEXT,
    id_employe      UUID REFERENCES employe(id_employe),
    date_changement TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 11. devis
-- ============================================================
CREATE TABLE IF NOT EXISTS devis (
    id_devis        SERIAL PRIMARY KEY,
    id_reparation   INT NOT NULL REFERENCES reparation(id_reparation) ON DELETE CASCADE,
    montant_ht      NUMERIC(10,2) NOT NULL,
    tva             NUMERIC(5,2)  NOT NULL DEFAULT 20.00,
    montant_ttc     NUMERIC(10,2) GENERATED ALWAYS AS (montant_ht * (1 + tva / 100)) STORED,
    accepte         BOOLEAN,
    date_creation   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_acceptation TIMESTAMPTZ
);

-- ============================================================
-- 12. facture
-- ============================================================
CREATE TABLE IF NOT EXISTS facture (
    id_facture      SERIAL PRIMARY KEY,
    id_reparation   INT NOT NULL REFERENCES reparation(id_reparation) ON DELETE CASCADE,
    numero_facture  VARCHAR(50) NOT NULL UNIQUE,
    montant_ht      NUMERIC(10,2) NOT NULL,
    tva             NUMERIC(5,2)  NOT NULL DEFAULT 20.00,
    montant_ttc     NUMERIC(10,2) GENERATED ALWAYS AS (montant_ht * (1 + tva / 100)) STORED,
    payee           BOOLEAN NOT NULL DEFAULT FALSE,
    date_emission   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_paiement   TIMESTAMPTZ
);

-- ============================================================
-- 13. mode_paiement
-- ============================================================
CREATE TABLE IF NOT EXISTS mode_paiement (
    id_mode_paiement SERIAL PRIMARY KEY,
    libelle          VARCHAR(50) NOT NULL UNIQUE  -- ex: Espèces, Carte, Virement
);

-- ============================================================
-- 14. paiement
-- ============================================================
CREATE TABLE IF NOT EXISTS paiement (
    id_paiement       SERIAL PRIMARY KEY,
    id_facture        INT NOT NULL REFERENCES facture(id_facture) ON DELETE CASCADE,
    id_mode_paiement  INT NOT NULL REFERENCES mode_paiement(id_mode_paiement),
    montant           NUMERIC(10,2) NOT NULL,
    date_paiement     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reference         VARCHAR(100)
);

-- ============================================================
-- 15. fournisseur
-- ============================================================
CREATE TABLE IF NOT EXISTS fournisseur (
    id_fournisseur  SERIAL PRIMARY KEY,
    nom             VARCHAR(150) NOT NULL,
    email           VARCHAR(150),
    telephone       VARCHAR(20),
    adresse         TEXT
);

-- ============================================================
-- 16. piece
-- ============================================================
CREATE TABLE IF NOT EXISTS piece (
    id_piece         SERIAL PRIMARY KEY,
    id_fournisseur   INT REFERENCES fournisseur(id_fournisseur),
    reference        VARCHAR(100) NOT NULL UNIQUE,
    designation      VARCHAR(200) NOT NULL,
    prix_unitaire    NUMERIC(10,2) NOT NULL,
    stock_disponible INT NOT NULL DEFAULT 0
);

-- ============================================================
-- 17. piece_reparation  (pièces utilisées dans une réparation)
-- ============================================================
CREATE TABLE IF NOT EXISTS piece_reparation (
    id_piece_reparation SERIAL PRIMARY KEY,
    id_reparation       INT NOT NULL REFERENCES reparation(id_reparation) ON DELETE CASCADE,
    id_piece            INT NOT NULL REFERENCES piece(id_piece),
    quantite            INT NOT NULL DEFAULT 1,
    prix_unitaire_applique NUMERIC(10,2) NOT NULL
);

-- ============================================================
-- 18. notification
-- ============================================================
CREATE TABLE IF NOT EXISTS notification (
    id_notification  SERIAL PRIMARY KEY,
    id_reparation    INT  REFERENCES reparation(id_reparation) ON DELETE SET NULL,
    id_employe       UUID REFERENCES employe(id_employe) ON DELETE SET NULL,
    message          TEXT NOT NULL,
    lue              BOOLEAN NOT NULL DEFAULT FALSE,
    date_creation    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- Index utiles pour les requêtes fréquentes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_reparation_client     ON reparation(id_client);
CREATE INDEX IF NOT EXISTS idx_reparation_employe    ON reparation(id_employe);
CREATE INDEX IF NOT EXISTS idx_reparation_boutique   ON reparation(id_boutique);
CREATE INDEX IF NOT EXISTS idx_reparation_statut     ON reparation(statut_actuel);
CREATE INDEX IF NOT EXISTS idx_reparation_date_depot ON reparation(date_depot);
CREATE INDEX IF NOT EXISTS idx_appareil_client       ON appareil(id_client);
CREATE INDEX IF NOT EXISTS idx_historique_reparation ON historique_statut(id_reparation);
CREATE INDEX IF NOT EXISTS idx_notification_employe  ON notification(id_employe);
CREATE INDEX IF NOT EXISTS idx_notification_lue      ON notification(lue);
