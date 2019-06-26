from grpc._cython import cygrpc
from arch.api.proto import proxy_pb2_grpc
from fate_flow.utils.api_utils import get_json_result
from flask import Flask
import grpc, time, sys
from concurrent import futures
from fate_flow.settings import IP, GRPC_PORT, HTTP_PORT, _ONE_DAY_IN_SECONDS, MAX_CONCURRENT_JOB_RUN, logger
from werkzeug.wsgi import DispatcherMiddleware
from werkzeug.serving import run_simple
from fate_flow.utils.grpc_utils import UnaryServicer
from fate_flow.apps.data_access import manager as data_access_manager
from fate_flow.apps.machine_learning_model import manager as model_manager
from fate_flow.driver.job_controller import manager as job_manager
from fate_flow.apps.dtable import manager as dtable_manager
from fate_flow.driver.scheduler import Scheduler
from fate_flow.manager.queue_manager import JOB_QUEUE
from fate_flow.storage.fate_storage import FateStorage

'''
Initialize the manager
'''

manager = Flask(__name__)


@manager.errorhandler(500)
def internal_server_error(e):
    logger.exception(e)
    return get_json_result(status=100, msg=str(e))


if __name__ == '__main__':
    FateStorage.init_storage()
    manager.url_map.strict_slashes = False
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10),
                         options=[(cygrpc.ChannelArgKey.max_send_message_length, -1),
                                  (cygrpc.ChannelArgKey.max_receive_message_length, -1)])

    proxy_pb2_grpc.add_DataTransferServiceServicer_to_server(UnaryServicer(), server)
    server.add_insecure_port("{}:{}".format(IP, GRPC_PORT))
    server.start()
    app = DispatcherMiddleware(
        manager,
        {
            '/data': data_access_manager,
            '/model': model_manager,
            '/job': job_manager,
            '/dtable': dtable_manager,
        }
    )
    scheduler = Scheduler(queue=JOB_QUEUE, concurrent_num=MAX_CONCURRENT_JOB_RUN)
    scheduler.start()
    run_simple(hostname=IP, port=HTTP_PORT, application=app, threaded=True)

    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)
        sys.exit(0)
