pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/FarooquiAun/ShoeStore.git'
            }
        }

        stage('Build Application') {
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
