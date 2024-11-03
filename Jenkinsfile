pipeline {
    agent any
    tools{
        maven 'maven'
    }
    stages{
        stage('Build maven'){
            steps{
               checkout([$class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    extensions: [], 
                    userRemoteConfigs: [[
                        url: 'https://github.com/nico-s-b/prestabanco-backend', 
                        credentialsId: 'github-credentials'
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
        stage('Push image to Docker Hub'){
            steps{
                script{
                   withCredentials([string(credentialsId: 'docker-credentials', variable: 'dhpsw')]) {
                        bat 'docker login -u nicolassepulvedab -p %dhpsw%'
                   }
                   bat 'docker push nicolassepulvedab/prestabanco-backend'
                }
            }
        }
    }
}