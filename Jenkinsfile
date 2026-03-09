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
                sh 'docker build -t farooquiaun/shoestore-app:latest .'
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                sh 'docker push farooquiaun/shoestore-app:latest'
            }
        }

    }
}

