#!/bin/bash
set -e

# Install Java
echo "Installing Java..."
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.2-open

# Make gradlew executable
echo "Making gradlew executable..."
chmod +x ./gradlew

# Run Gradle task
echo "Running Gradle task..."
./gradlew generateJavadoc

echo "JavaDoc generation complete!"