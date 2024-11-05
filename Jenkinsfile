pipeline {
    agent any
    tools{
        maven 'maven'
    }

    environment {
        DB_HOST = 'localhost'
        DB_PORT = '5432'     
        DB_USER = 'postgres' 
        DB_PASS = 'admin'
    }

    stages{
        stage('Build maven'){
            steps{
               checkout([$class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    extensions: [], 
                    userRemoteConfigs: [[
                        url: 'https://github.com/nico-s-b/prestabanco-backend', 
                        credentialsId: 'github-token'
                    ]]
                ])
                bat 'mvn clean install'
            }
        }

        stage('Unit Tests') {
            steps {
                // Run Maven 'test' phase. It compiles the test sources and runs the unit tests
                bat 'mvn test' // Use 'bat' for Windows agents or 'sh' for Unix/Linux agents
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t nicolassepulvedab/prestabanco-backend .'
                }
            }
        }
        stage('Push image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                    }
                    bat 'docker push nicolassepulvedab/prestabanco-backend'
                }
            }
        }
    }
}