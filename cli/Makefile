BINARY := diva-cli
BUILD_DIR := bin

.PHONY: build install test lint clean

build:
	go build -o $(BUILD_DIR)/$(BINARY) ./cmd/diva

install: build
	install -m 0755 $(BUILD_DIR)/$(BINARY) $(GOPATH)/bin/$(BINARY)

test:
	go test ./...

lint:
	go vet ./...

clean:
	rm -rf $(BUILD_DIR)
