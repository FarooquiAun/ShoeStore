pipeline {
    agent any

    stages {

        stage('Build Application') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t shoestore-app .'
            }
        }

    }
}

