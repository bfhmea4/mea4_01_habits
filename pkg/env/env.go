package env

import (
	"os"
)

var (
	// POCKETBASE_DATA_DIR is the default data directory
	POCKETBASE_DATA_DIR string
	// POCKETBASE_ENCRYPTION_KEY is the default encryption key
	POCKETBASE_ENCRYPTION_KEY string
	// HABITSUS_LOG_FILE_PATH is the default path to the log file
	HABITSUS_LOG_FILE_PATH string
)

// Init initializes the environment variables
func Init() error {
	if POCKETBASE_DATA_DIR = os.Getenv("POCKETBASE_DATA_DIR"); POCKETBASE_DATA_DIR == "" {
		POCKETBASE_DATA_DIR = "/pb_data"
	}

	if POCKETBASE_ENCRYPTION_KEY = os.Getenv("POCKETBASE_ENCRYPTION_KEY"); POCKETBASE_ENCRYPTION_KEY == "" {
		POCKETBASE_ENCRYPTION_KEY = "POCKETBASE_ENCRYPTION_KEY"
	}

	if HABITSUS_LOG_FILE_PATH = os.Getenv("HABITSUS_LOG_FILE_PATH"); HABITSUS_LOG_FILE_PATH == "" {
		HABITSUS_LOG_FILE_PATH = "./habitsus.log"
	}

	return nil
}
