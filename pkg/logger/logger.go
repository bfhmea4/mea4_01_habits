package logger

import (
	"io"
	"log"
	"os"

	"github.com/bfhmea4/mea4_01_habits/pkg/env"
)

func openLogFile(path string) (*os.File, error) {
	logFile, err := os.OpenFile(path, os.O_WRONLY|os.O_APPEND|os.O_CREATE, 0644)
	if err != nil {
		return nil, err
	}
	return logFile, nil
}

func SetupLogger() {
	file, err := openLogFile(env.HABITSUS_LOG_FILE_PATH)
	if err != nil {
		log.Fatal(err)
	}
	mw := io.MultiWriter(os.Stdout, file)
	log.SetOutput(mw)
	log.SetFlags(log.LstdFlags | log.Lshortfile | log.Lmicroseconds)

	log.Println("Application started")
}
