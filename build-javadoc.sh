#!/bin/bash
set -e

# Install Java
echo "Installing Java..."
curl -s "https://get.sdkman.io" | bash

# Use the correct path for the sdkman-init.sh file on Render
echo "Sourcing SDKMAN..."
source "/opt/render/.sdkman/bin/sdkman-init.sh"

# Accept license for Java
echo "Installing Java 21..."
echo "Y" | sdk install java 21.0.2-open

# Make gradlew executable
echo "Making gradlew executable..."
chmod +x ./gradlew

# Run Gradle task
echo "Running Gradle task..."
./gradlew generateJavadoc

echo "JavaDoc generation complete!"