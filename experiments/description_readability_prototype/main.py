import requests
import api
import pprint as pp
from typing import NamedTuple

request_active_projects_python = requests.get(
    url = api.ROOT_URL["production"] + api.ENDPOINTS["getActiveProjectsWithQuery"].format(query="python")
)

# pp = pprint.pprint(active_projects_python.json()["result"])
total_count = request_active_projects_python.json()["result"]["total_count"]  # What exactly is this count??
fetched_projects = request_active_projects_python.json()["result"]["projects"]

class Project(NamedTuple):
    id: int
    title: str
    description: str
    flesch_index: int

projects = [
    Project(
        id=project["id"], title=project["title"], description=project["preview_description"], flesch_index=0
    ) for project in fetched_projects
]

pp.pp(projects)