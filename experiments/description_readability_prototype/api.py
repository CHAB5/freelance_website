"""
This module lists the API roots and endpoints
"""

ROOT_URL = {
    "production": "https://www.freelancer.com/api/",
    "sandbox": "https://www.freelancer-sandbox.com/api/"
}

ENDPOINTS = {
    "getSelf": "users/0.1/self/",
    "getActiveProjectsWithQuery": "projects/0.1/projects/active/?query={query}",
    "getProjectByID": "projects/0.1/projects/{project_id}/",
    "getUserByID": "users/0.1/users/{user_id}/"
}

