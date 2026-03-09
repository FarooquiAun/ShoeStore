pipeline {
    agent any

    stages {

        stage('Build Application') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'ls -l target'
                sh 'docker build -t shoestore-app .'
            }
        }

    }
}

